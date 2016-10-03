package database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import models.MetaData;

/**
 *
 * @author Mateusz Gołosz
 */
public class DBHandler {
    private ExecutorService executor;
    private DAOImpl dao;
    
    private DBHandler(){
        super();
        executor = Executors.newSingleThreadExecutor();
        dao = new DAOImpl();
    }
    
    public static DBHandler getInstance(){
        return new DBHandler();
    }
    
    public void InsertTrack(MetaData metaData){
        executor.execute(new Runnable(){
            @Override
            public void run() {
                try {
                System.out.println("INSIDE INSERTTRACK");
                System.out.println(metaData);
                dao.insertTrack(metaData.getTrackName(), metaData.getPath(), metaData.getDurationInSec(), metaData.getAlbum(), metaData.getArtist(), metaData.getTags());
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("DATA " + metaData.toString());
                }
            }
        });
    }
    
    
}
