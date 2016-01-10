import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Bandit extends Entite {

	public Bandit(int position) {
		chargerImages();

		if (position == 1) {
			x = -100;
			vitesseX = -VITESSE_MARCHE;
		} else {
			x = 6100;
			vitesseX = VITESSE_MARCHE;
		}
	}

	public void chargerImages() {
		ArrayList<String> nomImages = new ArrayList<>();
		nomImages.add("droite1");
		nomImages.add("droiteMarche1");
		nomImages.add("droiteMarche2");
		nomImages.add("droiteGun1");
		nomImages.add("droiteGun2");
		nomImages.add("gauche1");
		nomImages.add("gaucheMarche1");
		nomImages.add("gaucheMarche2");
		nomImages.add("gaucheGun1");
		nomImages.add("gaucheGun2");

		for (String s : nomImages) {
			Image image = new ImageIcon("res/bandit/" + s + ".png").getImage();
			sprites.put(s, image);
		}
	}

}
