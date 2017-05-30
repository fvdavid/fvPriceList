package fv.monster.service;

import fv.monster.model.Account;
import fv.monster.repository.AccountRepository;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HttpSession httpSession;

    public final String CURRENT_USER_KEY = "CURRENT_USER";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByUserNameOrEmail(username, username);

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(account.getRole());

        return new org.springframework.security.core.userdetails.User(account.getUserName(), account.getPassword(), auth);
    }

    public void autoLogin(Account account) {
        autoLogin(account.getUserName());
    }

    public void autoLogin(String username) {
        UserDetails userDetails = this.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    public Account register(Account account) {
        account.setPassword(encodeUserPassword(account.getPassword()));

        if (this.accountRepository.findOneByUserName(account.getUserName()) == null
                && this.accountRepository.findOneByEmail(account.getEmail()) == null) {

            this.accountRepository.save(account);
            return account;
        }
        return null;
    }

    public Account registerNewAdmin(Account account) {
        account.setPassword(encodeUserPassword(account.getPassword()));

        if (this.accountRepository.findOneByUserName(account.getUserName()) == null
                && this.accountRepository.findOneByEmail(account.getEmail()) == null) {

            account.setRole("FV_ADMIN");
            this.accountRepository.save(account);
            return account;
        }
        return null;
    }

    public String encodeUserPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Account getLoggedInUser() {
        return getLoggedInUser(false);
    }

    public Account getLoggedInUser(Boolean forceFresh) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = (Account) httpSession.getAttribute(CURRENT_USER_KEY);
        if (forceFresh || httpSession.getAttribute(CURRENT_USER_KEY) == null) {
            user = this.accountRepository.findOneByUserName(userName);
            httpSession.setAttribute(CURRENT_USER_KEY, user);
        }
        return user;
    }

}
