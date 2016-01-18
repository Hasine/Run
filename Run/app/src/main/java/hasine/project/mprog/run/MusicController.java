package hasine.project.mprog.run;

import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context c){
        super(c);
        setPrevNextListeners();
    }

    private void setPrevNextListeners() {

    }

    public void hide(){}

}
