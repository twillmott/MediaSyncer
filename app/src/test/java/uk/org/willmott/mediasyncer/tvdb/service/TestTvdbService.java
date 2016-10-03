package uk.org.willmott.mediasyncer.tvdb.service;

import org.junit.Test;

import static org.junit.Assert.*;


import uk.org.willmott.mediasyncer.tvdb.model.ShowActors;

/**
 * Created by tomwi on 02/10/2016.
 */

public class TestTvdbService {

    @Test
    public void test() {
        TheTvdbService theTvdbService = new TheTvdbService();

        ShowActors a = theTvdbService.getShowActors("247808");

        assertNotNull(a);
    }
}
