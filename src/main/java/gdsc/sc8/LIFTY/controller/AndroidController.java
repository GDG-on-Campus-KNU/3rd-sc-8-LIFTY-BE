package gdsc.sc8.LIFTY.controller;

import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AndroidController {

    @GetMapping(value = "/.well-known/assetlinks.json", produces = "application/json")
    public ResponseEntity<String> createProduct() throws URISyntaxException {
        return ResponseEntity.ok().body(
            """
                [{
                  "relation": ["delegate_permission/common.handle_all_urls"],
                  "target": {
                    "namespace": "android_app",
                    "package_name": "page.lifty.gdsclifty",
                    "sha256_cert_fingerprints":
                    ["00:BA:E5:45:FE:D3:3A:DA:AC:06:19:FC:0D:DC:9F:94:C7:C2:A6:C0:F5:73:F5:47:96:6A:CF:E4:46:7A:E8:E9"]
                  }
                },
                {
                  "relation": ["delegate_permission/common.get_login_creds"],
                  "target": {
                    "namespace": "web",
                    "site": "https://dev.api.lifty.page"
                  }
                },
                {
                  "relation": ["delegate_permission/common.get_login_creds"],
                  "target": {
                    "namespace": "android_app",
                    "package_name": "page.lifty.gdsclifty",
                    "sha256_cert_fingerprints":
                    ["00:BA:E5:45:FE:D3:3A:DA:AC:06:19:FC:0D:DC:9F:94:C7:C2:A6:C0:F5:73:F5:47:96:6A:CF:E4:46:7A:E8:E9"]
                  }
                }]"""
        );
    }

}
