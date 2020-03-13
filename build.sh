#! /bin/sh
./gradlew clean
./gradlew aR;
java  -jar ../MTK5660_signature_tool_for_hisense_app/signapk.jar ../MTK5660_signature_tool_for_hisense_app/platform.x509.pem ../MTK5660_signature_tool_for_hisense_app/platform.pk8 ./app/release/app-release.apk ./apk/StoreMode_signed.apk
