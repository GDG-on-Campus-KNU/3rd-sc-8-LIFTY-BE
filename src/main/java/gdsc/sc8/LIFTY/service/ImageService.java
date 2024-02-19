package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.gemini.GeminiRequestDto;
import gdsc.sc8.LIFTY.DTO.gemini.GenerationConfig;
import gdsc.sc8.LIFTY.utils.gcp.Base64ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final Base64ConvertUtil base64ConvertUtil;

    public GeminiRequestDto toRequestDtoWithImage(String imageRequest, boolean isOauth) {
        List<GeminiRequestDto.Content> contents = new ArrayList<>();
        contents.add(convertMultiModal(imageRequest,isOauth));

        return new GeminiRequestDto(contents, new GenerationConfig());
    }

    //멀티모달 content 객체 생성
    private GeminiRequestDto.Content convertMultiModal(String imageRequest, boolean isOauth) {
        List<GeminiRequestDto.Part> parts = new ArrayList<>();
        GeminiRequestDto.Part prompt = new GeminiRequestDto.Part("이미지를 보고 짧게 리액션해주세요.");
        parts.add(prompt);

        if (isOauth){
            String imageUri = "gs://" + imageRequest.substring("https://storage.cloud.google.com".length()-1);
            GeminiRequestDto.Part.FileData fileData = new GeminiRequestDto.Part.FileData(
                    "image/png",
                    imageUri);

            GeminiRequestDto.Part image = new GeminiRequestDto.Part(fileData);
            parts.add(image);
        }
        else{
            String imageData = base64ConvertUtil.uriToBase64(imageRequest);
            GeminiRequestDto.Part.InlineData inlineData = new GeminiRequestDto.Part.InlineData("image/png",imageData);
            GeminiRequestDto.Part image = new GeminiRequestDto.Part(inlineData);
            parts.add(image);
        }

        return new GeminiRequestDto.Content("USER",parts);
    }

}
