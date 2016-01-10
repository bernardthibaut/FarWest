import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Jeu {

	Personnage perso = new Personnage();

	ArrayList<Entite> ennemis = new ArrayList<>();
	ArrayList<Entite> ennemisMorts = new ArrayList<>();

	HashMap<String, Image> backgrounds = new HashMap<>();

	int indiceBackground = 2;
	Image background;

	JPanel panel;

	public Jeu(Fenetre f) {
		panel = new EcranTitre(f, this);

		for (int i = 1; i <= 10; i++) {
			backgrounds.put("background" + i, new ImageIcon("res/background/background" + i + ".jpg").getImage());
		}

		background = backgrounds.get("background2");
	}

	public void spawnBandit() {
		ennemis.add(new Bandit(1));
	}

	public void updateBackground() {
		background = backgrounds.get("background" + indiceBackground);
	}

}
