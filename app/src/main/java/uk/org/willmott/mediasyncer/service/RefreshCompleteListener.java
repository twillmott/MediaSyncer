package uk.org.willmott.mediasyncer.service;

import java.util.List;

import uk.org.willmott.mediasyncer.model.Series;

/**
 * A listener interface to inform an activity that the a
 * refresh is complete.
 * <p>
 * Created by tomwi on 22/10/2016.
 */

public interface RefreshCompleteListener {
    public void refreshComplete(String result);
}
