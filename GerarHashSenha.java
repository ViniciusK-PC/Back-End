import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHashSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "Bancada402.";
        String hash = encoder.encode(senha);
        System.out.println("Hash BCrypt para 'Bancada402.':");
        System.out.println(hash);
    }
}
