package claudiopostiglione.gestionaleEventi.configClass;

import claudiopostiglione.gestionaleEventi.exceptions.CloudinaryException;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getImageUpLoader(@Value("${cloudinary.name}") String cloudName, @Value("${cloudinary.key}") String apiKey, @Value("${cloudinary.secret}") String apiSecret){

        //Controllo se i dati passati sono giusti e se ci sono
        try {
            System.out.println("Cloudname: " + cloudName);
            System.out.println("ApiKey: " + apiKey );
            System.out.println("ApiSecret: " + apiSecret);
        }catch (CloudinaryException ex){
            System.out.println("Attenzione, alcuni dati non sono stati passati");
        }

        Map<String,String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
