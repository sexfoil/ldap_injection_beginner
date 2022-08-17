import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.directory.SearchResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LdapInjectionTest {

    private SimpleLdapService service;


    @BeforeEach
    void setUp() {
        service = new SimpleLdapService();
    }

    @AfterEach
    void destroy() {
        service.shutDownServer();
    }


    @Test
    void authenticateUserWithCorrectInputTest() {
        String existingUserBobLogin = "bob";
        String existingUserBobPassword = "bobspassword";

        assertTrue(service.authenticateClient(existingUserBobLogin, existingUserBobPassword));
    }

    @Test
    void authenticateUserWithWrongInputTest() {
        String injectionInputLogin = "*)(uid=*))(|(uid=*";
        String anyPassword = "doesn't matter";

        assertFalse(service.authenticateClient(injectionInputLogin, anyPassword));
    }

    @Test
    void searchUserByUidWithCorrectInputTest() {
        String existingUid = "ben";

        List<SearchResult> result = service.searchByUid(existingUid);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("uid: " + existingUid, result.get(0).getAttributes().get("uid").toString());
    }

    @Test
    void searchUserByUidWithWrongInputTest() {
        String injectedSymbol = "*";

        List<SearchResult> result = service.searchByUid(injectedSymbol);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
