package mmt08.beswitched;
import java.io.IOException;
import java.util.Map;
import android.media.MediaPlayer;

// Sinlgeton-class
public class SoundManager {
	
	private static SoundManager instance;
	private Map<String , MediaPlayer> players;
	 
    private SoundManager() {}

    public synchronized static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    public void playSound(String name) {
    	if (players.get(name) == null) {
    		MediaPlayer mediaPlayer = new MediaPlayer();
    		try {
				mediaPlayer.setDataSource("sounds/" + name + ".mp3");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		players.put(name, mediaPlayer);
    	}
    	players.get(name).start();
    }

}
