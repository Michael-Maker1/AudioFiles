

	public class AudioFile{
		// Attribute
		private String dateipfad;
		private String dateiname;
		private String autor;
		private String titel;
	    private String extension;
		
		private char separatorChar = System.getProperty("file.separator").charAt(0);
		

	    public AudioFile() {}
		
		public AudioFile(String pfad) {
			parsePathname(pfad);
			parseFilename(getFilename());
		}

		private boolean isWindows() {
			return System.getProperty("os.name").toLowerCase().indexOf("win") >=0;
		}

		// andere Methoden
		private String replaceSeparator(String s, char separatorToReplace) {
			String result = "";
			
			for (int index = 0; index < s.length();) {
				int indexTrenner = s.indexOf(separatorToReplace, index);
				
				if (indexTrenner == -1) {
					// kein Trenner mehr
					result += s.substring(index); // Rest in Zielstring kopieren
					break;
				} else {
					result += s.substring(index, indexTrenner) + separatorChar;
					index = indexTrenner + 1;					
				}
			}
			return result;
		}
		
		public void parsePathname(String s) {
			/*
			 * Algorithmus:
			 * - Ersetze Pfadtrenner durch gültige für Betriebssystem
			 * - Behandle ggf. Laufwerkspräfix
			 *   => Speichere Ergebnis in dateipfad
			 * - dateiname := <alles von letztem Pfadtrenner bis zum Ende>
			 * 	 => Speichere Ergebnis in dateiname
			 */

			if(separatorChar == '/') {
				s = s.replace('\\', separatorChar);
			}
			else  {
				 s = s.replace('/', separatorChar);
			} boolean ersetzt  = true;
			String doppelseparator = "" + separatorChar + separatorChar;
			do {
				int index = s.indexOf(doppelseparator);
				ersetzt = false;
				if(index != -1) {
					s = s.substring(0, index) +  s.substring(index+1);
					ersetzt = true;
				}
				
			}
			while(ersetzt);
			
		
			
			if (!isWindows() && s.length() >= 2) {
				char zeichen1 = s.charAt(0);
				char zeichen2 = s.charAt(1);
			
				
				if ((zeichen1 >= 'a' && zeichen1 <= 'z' || zeichen1 >= 'A' && zeichen1 <= 'Z') && zeichen2 == ':') {
					s = "/" + zeichen1 + s.substring(2);
				}
			}
			dateipfad = s;
			
			int letzterPfadtrenner = s.lastIndexOf(separatorChar);
			
			if (letzterPfadtrenner == -1) {
				// kein Pfad
				dateiname = s;
			} else {
				dateiname = s.substring(letzterPfadtrenner + 1);
			}
		}
		
		public void parseFilename(String s) {
			/*
			 * Algorithmus:
			 * - minus suchen
			 * - falls gefunden: Autor und Titel speichern, bei Titel ggf. Extension entfernen
			 */
			
			if(s.equals("-")) {
				autor = "";
				titel = "-";}
			else if(s.equals(" - ")) {
				autor ="";
				titel ="";
				
			}
			else {
				int extensionIndex = s.lastIndexOf(".");
				if( extensionIndex >= 0) {
					extension = s.substring(extensionIndex +1);
					s = s.substring(0, extensionIndex);
			}
				int minusIndex = s.indexOf(" - ");
				if( minusIndex >=0) {
					autor = s.substring(0, minusIndex).trim();
					titel = s.substring(minusIndex+2).trim();
				}else {
					autor ="";
					titel = s.trim();
					
				}
			}
		}
		
		public String getPathname() {
			return dateipfad;
		}


		public String getFilename() {
			return dateiname;
		}

		public String getAuthor() {
			return autor;
		}

		public String getTitle() {
			return titel;
		}


		
		@Override
		 public String toString() {
			 if( getAuthor() != null && getAuthor().length() == 0) {
				 return getTitle();
			 }
			 else { return getAuthor() + " - " + getTitle();}
		 }
		

		public static void main(String[] args) {
			String[] pfade = {
					"d:\\medien\\mp3dateien\\Falco - Rock me Amadeus .mp3",
					"","-"," - ", ".mp3", "audiofile.aux", "/my-tmp/file.mp3",
					"d:\\\\part1\\\file.mp3",
					"Falco.mp3",
					"Falco - Rock me","Z:///file.mp3",
					"/mp3/AC_DC - Hell's bells.yeah!.mp3",
					"Z:\\\\part1\\\\\\\\file.mp3\\\\"
				};
			for (int i = 0; i < pfade.length; ++i) {
			AudioFile af = new AudioFile(pfade[i]);
			System.out.printf("pfad: %s, name: %s, autor: %s, titel: %s\n", 
					af.getPathname(), af.getFilename(), af.getAuthor(), af.getTitle());
				}
			}
	}

		


}
