package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.image.ImageResponseDto;
import gdsc.sc8.LIFTY.domain.ChatImage;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import gdsc.sc8.LIFTY.infrastructure.ChatImageRepository;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import gdsc.sc8.LIFTY.utils.gcp.DataBucketUtil;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatImageService {

    private final DataBucketUtil dataBucketUtil;
    private final UserRepository userRepository;
    private final ChatImageRepository chatImageRepository;

    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";
    private final static String BUCKET_NAME = "lifty-bucket";

    @Transactional
    public ImageResponseDto upload(String email, MultipartFile image) throws IOException {
        User user = userRepository.getUserByEmail(email);
        if (image.isEmpty()) {
            throw new NotFoundException(ErrorStatus.NO_IMAGE, ErrorStatus.NO_IMAGE.getMessage());
        }
        log.info("이미지 존재");
        if (image.getContentType() == null || !image.getContentType().startsWith("image")) {
            throw new NotFoundException(ErrorStatus.INVALID_FILE_TYPE, ErrorStatus.INVALID_FILE_TYPE.getMessage());
        }
        log.info("이미지 타입 확인 통과");
        ChatImage chatImage = uploadToBucket(user, image);
        return ImageResponseDto.convertToDto(chatImage);

    }

    private ChatImage uploadToBucket(User user, MultipartFile image) throws IOException {
        String imageUri = IMAGE_URI_PREFIX + dataBucketUtil.uploadToBucket(image);
        log.info("이미지 업로드 완료");
        ChatImage chatImage = ChatImage.builder()
            .user(user)
            .imageUri(imageUri)
            .build();
        chatImageRepository.save(chatImage);
        return chatImage;
    }
}
