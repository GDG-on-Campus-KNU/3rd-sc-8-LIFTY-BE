package gdsc.sc8.LIFTY.DTO.image;

import gdsc.sc8.LIFTY.domain.ChatImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponseDto {

    private Long id;
    private String imageUri;

    public static ImageResponseDto convertToDto(ChatImage chatImage) {
        return ImageResponseDto.builder()
            .id(chatImage.getId())
            .imageUri(chatImage.getImageUri())
            .build();
    }
}
