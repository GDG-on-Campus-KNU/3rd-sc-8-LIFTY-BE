package gdsc.sc8.LIFTY.DTO.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {

    private String name;
    private String profileUri;
    private Integer level;
    private Long exp;

    @Builder
    public UserInfoResponseDto(String name, String profileUri, Integer level, Long exp) {
        this.name = name;
        this.profileUri = profileUri;
        this.level = level;
        this.exp = exp;
    }

}
