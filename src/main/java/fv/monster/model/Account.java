
package fv.monster.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@Entity
@Table(name = "account")
public class Account {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100, message = "Username must at least 3 characters.")
    @Column(name = "userName")
    private String userName;

    @NotNull
    @Size(min = 3, max = 100, message = "Password must at least 3 characters.")
    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @Email(message = "Email address is not valid.")
    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;

    @Column(name = "role")
    private String role = "FV_USER";
    
    @Column(name = "address")
    private String address;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "profilePicture")
    private String profilePicture;
    
    
    @OneToMany(mappedBy = "accountId")
    private List<Materi> daftarMateri = new ArrayList<>();

    public List<Materi> getDaftarMateri() {
        return daftarMateri;
    }

    public void setDaftarMateri(List<Materi> daftarMateri) {
        this.daftarMateri = daftarMateri;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean isAdmin() {
        return this.role.equals("ROLE_ADMIN");
    }

    public Boolean isMatchingPasswords() {
        return this.password.equals(this.confirmPassword);
    }
}
