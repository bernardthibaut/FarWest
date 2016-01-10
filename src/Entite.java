import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Entite {

	Random r = new Random();

	final double VITESSE_MARCHE = 2.0;

	HashMap<String, Image> sprites = new HashMap<>();
	String spriteActuel = "gauche1";

	int indexSprite = 1;
	int compteurSpriteDeplacement = 0;

	double x = 950.0;
	double y = 920.0;

	double vitesseX = 0.0;
	double boostVitesse = 1.0;

	int isShoot = 0;
	ArrayList<Balle> balles = new ArrayList<>();
	ArrayList<Balle> ballesSupr = new ArrayList<>();

	Rectangle hitbox;

	public Image getSprite() {
		return sprites.get(spriteActuel);
	}

	public void changerSprite(String nomSprite) {
		Image sprite = sprites.get(nomSprite);
		if (sprite != null) {
			spriteActuel = nomSprite;
		}
	}

	public void marcherDroite() {
		if (spriteActuel == "droite1") {
			changerSprite("droiteMarche" + indexSprite);
			if (indexSprite == 1)
				indexSprite = 2;
			else
				indexSprite = 1;
		} else {
			changerSprite("droite1");
		}
	}

	public void marcherGauche() {
		if (spriteActuel == "gauche1") {
			changerSprite("gaucheMarche" + indexSprite);
			if (indexSprite == 1)
				indexSprite = 2;
			else
				indexSprite = 1;
		} else {
			changerSprite("gauche1");
		}
	}

	public void updateHitbox() {
		if (spriteActuel.contains("droite"))
			hitbox = new Rectangle((int) x, (int) y, 40, 78);
		else if (spriteActuel.contains("gauche"))
			hitbox = new Rectangle((int) x + 35, (int) y, 40, 78);
	}

	public double getVitesse() {
		return vitesseX * boostVitesse;
	}

	public void deplacementIA() {
		if (r.nextInt(100) == 0)
			vitesseX = (-1 * r.nextInt(2)) * VITESSE_MARCHE;

		if (x < 60)
			vitesseX = -VITESSE_MARCHE;
		else if (x > 5940)
			vitesseX = VITESSE_MARCHE;

	}

	public void tirer() {
		isShoot = 25;
		balles.add(new Balle(this));
	}

	public void spriteTir() {
		if (spriteActuel.contains("droite")) {
			changerSprite("droiteGun2");
		} else {
			changerSprite("gaucheGun2");
		}
	}
}
