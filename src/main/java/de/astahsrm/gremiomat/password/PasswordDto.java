package de.astahsrm.gremiomat.password;

import javax.validation.constraints.NotBlank;

import de.astahsrm.gremiomat.constraints.fieldsvaluematch.FieldsValueMatch;
import de.astahsrm.gremiomat.constraints.validpassword.ValidPassword;

@FieldsValueMatch(field = "newPassword", fieldMatch = "verifyPassword")
public class PasswordDto {
    
    @ValidPassword
    private String newPassword;

    @NotBlank
    private String verifyPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

}
