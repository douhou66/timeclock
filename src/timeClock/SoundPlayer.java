package timeClock;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

public class SoundPlayer implements Runnable
{
    private AudioClip audioClip;
    File file;
    URL url;
    private Thread playThread; //启动播放音频线程
    private Thread controlThread; //控制音频的播放、停止
    private volatile int playTime = 2000;
    private final String UNPLAYED_STATUS = "unplayed_status";
    private final String PLAYING_STATUS = "playing_status";
    private final String STOP_STATUS = "stop_status";
    private volatile boolean isTerminated = true;
    private final String PAUSE_STATUS = "pause_status";
    private volatile String status = UNPLAYED_STATUS;
    private boolean isPlayed;
    private boolean isLoop;

    public boolean isLoop()
    {
        return isLoop;
    }

    public void setIsLoop(boolean isLoop)
    {
        this.isLoop = isLoop;
    }

    /**
     * 终止音频的播放，同时所有播放资源都将释放
     */
    public void stop()
    {
        setStatus(STOP_STATUS);
        isTerminated = true;
        audioClip.stop();
        isPlayed=false;
//        System.exit(0);
    }

    public void play()
    {
        if (isPlayed)
        {
            resume();
            return;
        }
        isTerminated=false;
        playThread = new Thread(this);
        controlThread = new Thread()
        {
            @Override
            public void run()
            {
//                    super.run();
                while (!isTerminated)
                {
//                    System.out.println("status:" + status);
                    switch (status)
                    {

                        case PLAYING_STATUS:
                            synchronized (SoundPlayer.this)
                            {
                                SoundPlayer.this.notifyAll();
                            }
                            break;
                    }
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }


                }
                /**
                 * 等待播放线程状态改变
                 */
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                synchronized (SoundPlayer.this)
                {
                    SoundPlayer.this.notifyAll();
                }
                System.out.println("控制线程终止");
            }
        };
//        setStatus(PLAYING_STATUS);
        isTerminated = false;
        setStatus(PLAYING_STATUS);
        playThread.start();
        controlThread.start();
        isPlayed = true;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * 应该由播放音频的线程调用，每次播放完成之后等待被调用
     */
    private void pause()
    {
        synchronized (this)
        {
//            setStatus(PAUSE_STATUS);
            try
            {
                this.wait(0);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
//            audioClip.stop();
        }

    }

    public String getStatus()
    {
        return status;
    }

    public SoundPlayer(String filePath)
    {
        file = new File(filePath);
        try
        {
            if (!file.exists())
            {
                throw new NullPointerException("音频文件不存在");
            }
            String absolutePath = file.getAbsolutePath().replace("\\", "/");
            url = new URL("file:/" + absolutePath);
//            System.out.println(url);
            audioClip = Applet.newAudioClip(url);
        }
        catch (MalformedURLException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        // TODO Auto-generated method stub
        while (!isTerminated)
        {
//            setStatus(PLAYING_STATUS);
//            System.out.println("启动");
            if (status.equals(PLAYING_STATUS))
            {
                synchronized (this)
                {
//                    System.out.println("prestatus:" + status);
                    if (!isLoop)
                    {
                        setStatus(PAUSE_STATUS);
//                    System.out.println("is playing");
                        audioClip.play();
                        pause();
                    }
                    else
                    {
                        audioClip.loop();
//                        pause();
                        break;
                    }
//                    System.out.println("afterstatus:" + status);

                }
            }
          /*  try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }*/
        }
        System.out.println("播放线程终止");

    }

    public void resume()
    {
        setStatus(PLAYING_STATUS);
    }

    public static void main(String[] args)
    {

        SoundPlayer soundPlayer = new SoundPlayer(
                "soundTime/公鸡叫声.wav");
//        soundPlayer.setIsLoop(true);
        soundPlayer.play();
        Timer timer = new Timer();
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        soundPlayer.stop();
        try
        {
            soundPlayer.finalize();
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }


       /* try
        {
            //当前工作目录就是项目的根目录，可以使用符号.代表当前工作目录或者省略
            File file = new File("text.txt");
            if (!file.exists())
            {
                file.createNewFile();
                System.out.println(file.getAbsolutePath());
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
}
