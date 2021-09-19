package de.astahsrm.gremiomat;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.astahsrm.gremiomat.password.PasswordServiceImpl;

@SpringBootTest
class GremiomatApplicationTests {

	@Test
	public void whenPasswordGeneratedUsingCommonsLang3_thenSuccessful() {
		String password = new PasswordServiceImpl().generatePassword();
		int numCount = 0;
		for (char c : password.toCharArray()) {
			if (c >= 48 || c <= 57) {
				numCount++;
			}
		}
		assertTrue(numCount >= 2, "Password validation failed in commons-lang3");
	}

}
