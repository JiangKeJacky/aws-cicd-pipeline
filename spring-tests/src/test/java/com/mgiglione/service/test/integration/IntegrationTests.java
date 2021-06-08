package com.mgiglione.service.test.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
        MangaControllerIntegrationTest.class,
        MangaServiceIntegrationTest.class
})
@RunWith(Suite.class)
public class IntegrationTests
{
}
