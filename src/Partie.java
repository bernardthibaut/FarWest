import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Partie extends JPanel {

	Random r = new Random();
	Fenetre f;
	Jeu jeu;
	boolean gauchePressed = false;
	boolean droitePressed = false;
	Image controles = new ImageIcon("res/controles.png").getImage();
	double xBackground = 0;
	double acceleration = 0.2;
	boolean started = false;
	int tauxSpawn = 200;
	int obscurite = 2000;
	boolean estJour = true;

	// Chrono en secondes
	int chrono = 0;
	// Compteur en millisecondes
	int compteurChrono = 0;

	public Partie(Fenetre f, Jeu jeu, double xBackground) {
		this.f = f;
		this.jeu = jeu;
		this.xBackground = xBackground;
		setLayout(null);
		repaint();

		ajouterListeners();
		lancer();
	}

	public void paint(Graphics g) {
		g.drawImage(jeu.background, (int) xBackground, 0, null);

		g.drawImage(jeu.perso.getSprite(), (int) jeu.perso.x, (int) jeu.perso.y, null);

		for (Entite e : jeu.ennemis) {
			g.drawImage(e.getSprite(), (int) e.x, (int) e.y, null);
		}

		for (Balle b : jeu.perso.balles) {
			g.fillRect((int) b.x, (int) b.y, b.largeur, b.hauteur);
		}

		g.drawImage(controles, 450, 0, null);
	}

	public void ajouterListeners() {

		f.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				int button = e.getButton();

				if (button == MouseEvent.BUTTON1) {
					if (jeu.perso.isShoot == 0) {
						jeu.perso.tirer();
						try {
							jouerSon("gunshot.wav");
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
					}
				}
			}

		});

		f.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_Q || key == KeyEvent.VK_LEFT) {
					gauchePressed = true;
					jeu.perso.vitesseX = jeu.perso.VITESSE_MARCHE;
				}

				if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
					droitePressed = true;
					jeu.perso.vitesseX = -jeu.perso.VITESSE_MARCHE;
				}

				if (key == KeyEvent.VK_SHIFT) {
					jeu.perso.boostVitesse = 3.0;
				}

				if (key == KeyEvent.VK_SPACE) {
					if (!jeu.perso.jumping)
						jeu.perso.gravite = -7.0;
					jeu.perso.jumping = true;
				}

				if (key == KeyEvent.VK_1) {
					if (jeu.perso.isShoot == 0) {
						jeu.perso.tirer();
						try {
							jouerSon("gunshot.wav");
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_Q || key == KeyEvent.VK_LEFT) {
					gauchePressed = false;
				}

				if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
					droitePressed = false;
				}

				if (key == KeyEvent.VK_SHIFT) {
					jeu.perso.boostVitesse = 1.0;
				}

				if (!gauchePressed && !droitePressed)
					jeu.perso.vitesseX = 0.0;
			}

		});

	}

	public void lancer() {

		Timer timer = new Timer();

		TimerTask task = new TimerTask() {

			public void run() {

				compteurChrono += 10;
				if (compteurChrono >= 1000) {
					chrono++;
					compteurChrono = 0;
				}

				if (jeu.perso.jumping) {
					jeu.perso.addGraviteToY();
					jeu.perso.gravite += acceleration;
				} else {
					jeu.perso.gravite = 0.0;
				}

				if (chrono % 5 == 0 && compteurChrono == 0) {
					jeu.spawnBandit();
				}

				if (estJour) {
					obscurite++;
					if (obscurite % 1000 == 0 && obscurite <= 10000) {
						jeu.indiceBackground = obscurite / 1000;
						jeu.updateBackground();
						if (jeu.indiceBackground == 10)
							estJour = false;
					}
				} else {
					obscurite--;
					if (obscurite % 1000 == 0 && obscurite >= 2000) {
						jeu.indiceBackground = obscurite / 1000;
						jeu.updateBackground();
						if (jeu.indiceBackground == 2)
							estJour = true;
					}
				}

				defilementBackground();
				updateSprite(jeu.perso);
				jeu.perso.updateHitbox();

				for (Entite e : jeu.ennemis) {
					// e.deplacementIA();
					e.x -= e.getVitesse();
					updateSprite(e);
					e.updateHitbox();
					
					if(r.nextInt(50) == 0)
						e.tirer();
					
					for (Balle b : e.balles) {
						b.deplacer();
					}
					for (Balle b : e.ballesSupr) {
						b.suprimmer();
					}
				}

				for (Balle b : jeu.perso.balles) {
					b.deplacer();
					for (Entite e : jeu.ennemis) {
						if (b.hitbox.intersects(e.hitbox)) {
							jeu.ennemisMorts.add(e);
							jeu.perso.ballesSupr.add(b);
						}
					}
				}
				for (Balle b : jeu.perso.ballesSupr) {
					b.suprimmer();
				}

				for (Entite e : jeu.ennemisMorts) {
					jeu.ennemis.remove(e);
				}

				repaint();

			}

		};

		timer.scheduleAtFixedRate(task, 0, 10);

	}

	public void defilementBackground() {

		if (xBackground == 0) {
			if (jeu.perso.x - jeu.perso.getVitesse() > -20)
				jeu.perso.x -= jeu.perso.getVitesse();

			if (jeu.perso.x >= f.largeur / 2)
				xBackground += jeu.perso.getVitesse();

		} else if (xBackground == f.largeur - 6000) {
			if (jeu.perso.x - jeu.perso.getVitesse() < f.largeur - 60)
				jeu.perso.x -= jeu.perso.getVitesse();

			if (jeu.perso.x <= f.largeur / 2)
				xBackground += jeu.perso.getVitesse();

		} else {
			xBackground += jeu.perso.getVitesse();

			if (xBackground < f.largeur - 6000 || xBackground > 0) {
				if (xBackground < f.largeur - 6000) {
					xBackground = f.largeur - 6000;
				} else if (xBackground > 0) {
					xBackground = 0;
				}

			} else {
				for (Entite e : jeu.ennemis) {
					e.x += jeu.perso.getVitesse();
				}
			}

			for (Balle b : jeu.perso.balles) {
				b.x += jeu.perso.getVitesse();
			}

			for (Entite e : jeu.ennemis) {
				for (Balle b : e.balles) {
					b.x += jeu.perso.getVitesse();
				}
			}

		}
	}

	public void updateSprite(Entite e) {

		if (e.getVitesse() != 0) {
			if (e.compteurSpriteDeplacement % (int) (15 / (e.boostVitesse / 2)) == 0) {
				if (e.getVitesse() < 0)
					e.marcherDroite();
				else if (e.getVitesse() > 0)
					e.marcherGauche();
			}
			e.compteurSpriteDeplacement++;
		} else {
			e.compteurSpriteDeplacement = 0;
			if (e.spriteActuel.contains("droite"))
				e.changerSprite("droite1");
			else
				e.changerSprite("gauche1");
		}

		if (e.isShoot > 0) {
			e.spriteTir();
			e.isShoot--;
		}

	}

	public void jouerSon(String nomFichier)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		URL url = Partie.class.getResource("../res/" + nomFichier);
		final Clip clip = AudioSystem.getClip();
		try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(url)) {
			clip.open(audioIn);
		}
		clip.start();
	}

}
