package de.astahsrm.gremiomat.password;

import de.astahsrm.gremiomat.constraints.validpassword.ValidPassword;

public class PasswordDto {
    
    @ValidPassword
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
