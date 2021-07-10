package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CardsOfOmen;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Service

public class AccountServicesImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    public AccountServicesImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public boolean register(RegisterPojo registerPojo, String url) {

        if (registerPojo != null && !isLoginTaken(registerPojo.getLogin())) {

            Account account = createAccountFromRegisterPojo(registerPojo);
            accountRepository.save(account);

            try {
                verificationEmail(account, url);
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return true;

        }

        return false;
    }

    @Override
    public void verificationEmail(Account user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "GarrisonTracker@gmail.com";
        String senderName = "GarrisonTracker";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "GarrisonTracker";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getLogin());
        String verifyURL = siteURL + "verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public boolean verify(String verificationCode) {
        Optional<Account> optionalAccount = accountRepository.findByVerificationCode(verificationCode);

        if (optionalAccount.isEmpty() || optionalAccount.get().isEnabled()) {
            return false;
        } else {
            Account account = optionalAccount.get();
            account.setVerificationCode(null);
            account.setEnabled(true);
            accountRepository.save(account);
            return true;
        }
    }

    @Override
    public boolean addCards(CardsOfOmen cards) {
        if (cards.getAccount() != null) {
            Account account = cards.getAccount();
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCards(CardsOfOmen cards) {
        if (cards.getAccount() != null) {
            Account account = findById(cards.getAccount().getId());
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public Long sumEntriesGarrisonResources(Long accountId) {
        return Objects.requireNonNullElse(accountRepository.sumEntriesGarrisonResourcesByAccountId(accountId), 0L);
    }

    @Override
    public Long sumEntriesWarPaint(Long accountId) {
        return Objects.requireNonNullElse(accountRepository.sumEntriesWarPaintByAccountId(accountId), 0L);
    }

    @Override
    public Long countEntries(Long accountId) {
        return accountRepository.countEntriesByAccountId(accountId);
    }

    @Override
    public Long countDays(Long accountId) {
        return accountRepository.countDaysByAccountId(accountId);
    }

    @Override
    public boolean isEmailTaken(String s) {
       return accountRepository.existsByEmail(s);
    }

    private Account createAccountFromRegisterPojo(RegisterPojo registerPojo) {

        Account account = new Account();
        account.getOptions().setServer(registerPojo.getServer());
        account.setPassword(passwordEncoder.encode(registerPojo.getPassword()));
        account.setLogin(registerPojo.getLogin());
        account.setEnabled(false);
        account.setEmail(registerPojo.getEmail());
        account.setVerificationCode(RandomString.make(64));

        return account;
    }

    @Override
    public boolean save(Account account) {
        if (account != null) {
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public boolean isLoginTaken(String login) {
        if (login == null) {
            return false;
        }
        return accountRepository.existsByLogin(login);
    }

    @Override
    public Account findAccountByLogin(String login) {
        Optional<Account> optionalAccount = accountRepository.findByLogin(login);
        return optionalAccount.orElse(null);

    }

    @Override
    public Account attemptToLogIn(String login, String password) {

        Account account = this.findAccountByLogin(login);

        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            return account;
        }

        return null;
    }

    @Override
    public Account findById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.orElse(null);
    }


}

