package tasks;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import view.WindowManager;

/**
 *
 * @author Mateusz Gołosz
 */
public class ButtonLockTask extends TimerTask {
    
    int seconds;
    Boolean lock = false;
    WindowManager windowManager;
    
    public ButtonLockTask(Boolean lock, WindowManager windowManager){
        this.lock = lock;
        this.windowManager = windowManager;
    }
    
    public Boolean getLock(){
        return this.lock;
    }
    
    public void setLock(Boolean lock){
        this.lock = lock;
    }
    
    
    @Override
    public void run() {
        setLock(false);
        windowManager.setLocker(Boolean.FALSE);
    }
    
}
