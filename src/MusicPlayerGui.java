import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class MusicPlayerGui extends JFrame {
    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private final MusicPlayer musicPlayer;

    // For accessing file explorer
    private final JFileChooser fileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackButtons;

    public MusicPlayerGui() {
        // JFrame constructor: set title header to "Music Player"
        super("Music Player");

        // Set width and height
        setSize(400, 600);

        // End process when frame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Window on center of screen
        setLocationRelativeTo(null);

        // Frame cannot be resized by user
        setResizable(false);

        setLayout(null);

        // Frame Color
        getContentPane().setBackground(FRAME_COLOR);

        // Instantiate MusicPlayer class
        musicPlayer = new MusicPlayer();

        // File explorer
        fileChooser = new JFileChooser();
        // Default folder is "assets"
        fileChooser.setCurrentDirectory(new File("src/assets"));
        // Show only mp3 files
        fileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        // Background image
        JLabel songImage = new JLabel(loadImage("src/assets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        // Song title
        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        // Song artist
        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 24));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        // Slider
        JSlider playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 - 300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        add(playbackSlider);

        addPlaybackButtons();
    }

    private void addToolbar() {
        JToolBar toolbar = new JToolBar();

        // Toolbar position
        toolbar.setBounds(0, 0, getWidth(), 20);

        // Prevent toolbar from being moved
        toolbar.setFloatable(false);

        // Dropdown bar
        JMenuBar menuBar = new JMenuBar();
        toolbar.add(menuBar);

        // Song Menu
        JMenu songMenu = new JMenu("Song");
        menuBar.add(songMenu);

        // Item for loading song
        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(MusicPlayerGui.this);
                File selectedFile = fileChooser.getSelectedFile();

                if(selectedFile != null) {
                    Song song = new Song(selectedFile.getPath());

                    musicPlayer.loadSong(song);

                    updateSongTitleAndArtist(song);

                    pauseButtonToggle();
                }
            }
        });
        songMenu.add(loadSong);

        // Playlist Menu

        JMenu playlistMenu = new JMenu("Playlist");
        menuBar.add(playlistMenu);

        // Item for playlist creation
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        playlistMenu.add(createPlaylist);

        // Item for playlist loading
        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        playlistMenu.add(loadPlaylist);

        add(toolbar);
    }

    private void updateSongTitleAndArtist(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    private void pauseButtonToggle() {
        // Retrieve playback button reference (first index = play button)
        JButton playButton = (JButton) playbackButtons.getComponent(1);
        JButton pauseButton = (JButton) playbackButtons.getComponent(2);

        playButton.setVisible(false);
        playButton.setEnabled(false);

        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    private void playButtonToggle() {
        // Retrieve playback button reference (first index = play button)
        JButton playButton = (JButton) playbackButtons.getComponent(1);
        JButton pauseButton = (JButton) playbackButtons.getComponent(2);

        playButton.setVisible(true);
        playButton.setEnabled(true);

        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private ImageIcon loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));

            return new ImageIcon(image);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addPlaybackButtons() {
        playbackButtons = new JPanel();
        playbackButtons.setBounds(0, 435, getWidth() - 10, 80);
        playbackButtons.setBackground(null);

        // Prev button
        JButton prevButton = new JButton(loadImage("src/assets/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        playbackButtons.add(prevButton);

        // Play button
        JButton playButton = new JButton(loadImage("src/assets/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playbackButtons.add(playButton);

        // Pause button
        JButton pauseButton = new JButton(loadImage("src/assets/pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonToggle();

                musicPlayer.pauseSong();
            }
        });
        playbackButtons.add(pauseButton);

        // Next button
        JButton nextButton = new JButton(loadImage("src/assets/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        playbackButtons.add(nextButton);

        add(playbackButtons);
    }
}


