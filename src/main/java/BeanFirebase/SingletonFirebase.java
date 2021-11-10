package BeanFirebase;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Scope("singleton")
public class SingletonFirebase {
	private static SingletonFirebase instancia = null;
	
	private SingletonFirebase() {
		
	}
	
	public static SingletonFirebase getInstancia() throws IOException {
		if (instancia == null) {
			instancia = new SingletonFirebase();
		}
		arrancar();
		return instancia;
	}
	
	public static void arrancar() throws IOException {
		if (FirebaseApp.getApps().isEmpty()) {
			init();
		}
	}
	
	@PostConstruct
	@Bean
	public static void init() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/java/Resource/yendo-5c371-firebase-adminsdk-rczst-500b815097.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .build();
		System.out.println("ENTRO AL SINGLETON FIREBASE");
		FirebaseApp.initializeApp(options);
	}
}
