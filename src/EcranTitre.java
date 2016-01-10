import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EcranTitre extends JPanel {

	Fenetre f;
	Jeu jeu;

	Image titre = new ImageIcon("res/titre.png").getImage().getScaledInstance(750, 435, Image.SCALE_SMOOTH);

	double xBackground = 0;

	float transparenceTitre = 0f;

	int index = 0;

	boolean started = false;

	public EcranTitre(Fenetre f, Jeu jeu) {
		this.f = f;
		this.jeu = jeu;
		setLayout(null);
		repaint();

		lancer();

		f.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e) {
				started = true;
			}

		});
	}

	public void paint(Graphics g) {
		g.drawImage(jeu.background, (int) xBackground, 0, null);

		g.drawImage(jeu.perso.getSprite(), 950, 920, null);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparenceTitre));

		g2d.drawImage(titre, 1920 / 2 - 750 / 2, 75, null);

		g2d.setFont(new Font("helvetica", 0, 41));
		g2d.drawString("PRESS ANY KEY TO START", 680, 675);
	}

	public void lancer() {

		Timer timer = new Timer();

		TimerTask task = new TimerTask() {

			public void run() {

				if (!started) {
					if (transparenceTitre < 0.99f)
						transparenceTitre += 0.003f;
					else
						transparenceTitre = 1f;

					scrollDroite();

					if (index % 25 == 0) {
						jeu.perso.marcherDroite();
					}
					index++;
				} else {
					jeu.perso.changerSprite("droite1");
					jeu.panel = new Partie(f, jeu, xBackground);
					f.setContentPane(jeu.panel);
					f.revalidate();
					f.repaint();
					this.cancel();
				}

				repaint();

			}

		};

		timer.scheduleAtFixedRate(task, 0, 10);

	}

	public void scrollDroite() {
		if (xBackground > -(6000 - f.largeur)) {
			xBackground -= 0.3;
		} else {
			xBackground = f.largeur - 2000;
		}
	}
}
