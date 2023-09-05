package com.triplog.api;

import com.triplog.api.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class BaseTest {

    @Autowired
    protected DatabaseCleanup databaseCleanup;

    @BeforeEach
    protected void basicSetUp() {
        databaseCleanup.execute();
    }
}
