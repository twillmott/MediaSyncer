package uk.org.willmott.mediasyncer.imdb.service;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by tomwi on 02/10/2016.
 */

public class TestImdbService {

    @Test
    public void testGetImage() {

        // Given
        String name = "Tom Hanks";
        ImdbService imdbService = new ImdbService();

        // When
        String image = imdbService.getActorImage(name);

        // THen
        assertNotNull(image);
    }
}
