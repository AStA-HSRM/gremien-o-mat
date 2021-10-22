package de.astahsrm.gremiomat.password;

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
