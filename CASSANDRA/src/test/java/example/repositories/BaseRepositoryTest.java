package example.repositories;

import org.junit.jupiter.api.Test;

class BaseRepositoryTest {

    @Test
    void initSession() {
        BaseRepository repo = new BaseRepository();
        repo.initSession();

        repo.initKeyspace();
    }
}