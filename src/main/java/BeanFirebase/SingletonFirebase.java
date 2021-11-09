package BeanFirebase;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javax.annotation.PostConstruct;

@Scope("singleton")
public class SingletonFirebase {

	@PostConstruct
	@Bean
	public void init() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/java/Resource/yendo-5c371-firebase-adminsdk-rczst-500b815097.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .build();
		System.out.println("ALOJA");
		FirebaseApp.initializeApp(options);
	}
}
