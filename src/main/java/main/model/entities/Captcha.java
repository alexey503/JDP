package main.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="captcha_codes")
public class Captcha
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private String code;

    @Column(name="secret_code", nullable = false, columnDefinition = "TINYTEXT")
    private String secretCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
