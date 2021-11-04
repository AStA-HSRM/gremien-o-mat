package de.astahsrm.gremiomat.password;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.astahsrm.gremiomat.mgmt.MgmtUser;

@Entity
public class PasswordToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private MgmtUser user;

    @NotNull
    private Date expiryDate;

    public PasswordToken() {
        this.token = "";
    }

    public PasswordToken(String token, MgmtUser user, Date expDate) {
        super();
        setToken(token);
        setUser(user);
        setExpiryDate(expDate);
    }

    public static int getExpiration() {
        return EXPIRATION;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public MgmtUser getUser() {
        return user;
    }

    public void setUser(MgmtUser user) {
        this.user = user;
    }

}
