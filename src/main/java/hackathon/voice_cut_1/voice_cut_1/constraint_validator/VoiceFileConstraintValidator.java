package hackathon.voice_cut_1.voice_cut_1.constraint_validator;

import hackathon.voice_cut_1.voice_cut_1.annotation.VoiceFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class VoiceFileConstraintValidator implements ConstraintValidator<VoiceFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return false;
        }

        String fileName = multipartFile.getOriginalFilename();

        return fileName != null && fileName.toLowerCase().endsWith("." + "m4a");
    }
}
