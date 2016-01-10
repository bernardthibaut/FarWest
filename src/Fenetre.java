import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Fenetre extends JFrame {

	private Jeu jeu = new Jeu(this);
	int largeur = 1920, hauteur = 1080;

	private Clip clip;

	boolean musiqueChargee = false;

	public Fenetre() {
		// setPreferredSize(new Dimension(largeur, hauteur));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setContentPane(jeu.panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			chargerMusique();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

		ajouterListeners();

		pack();
		setVisible(true);
	}

	public void ajouterListeners() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				if (musiqueChargee)
					clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		});

		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_ESCAPE) {
					System.exit(1);
				}

				if (key == KeyEvent.VK_M)
					clip.stop();

				if (key == KeyEvent.VK_F1) {
					jeu.indiceBackground = 1;
					jeu.updateBackground();
				}

				if (key == KeyEvent.VK_F2) {
					jeu.indiceBackground = 2;
					jeu.updateBackground();
				}
			}

		});
	}

	public void chargerMusique() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		clip = AudioSystem.getClip();
		URL url = Fenetre.class.getResource("../res/song.wav");

		try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(url)) {
			clip.open(audioIn);
			musiqueChargee = true;
		}
	}

	// LANCEMENT DU JEU

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		new Fenetre();
	}
}
