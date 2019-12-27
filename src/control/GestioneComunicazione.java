package control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


import bean.Annuncio;
import bean.AnnuncioCompleto;
import bean.Medico;
import bean.Messaggio;
import bean.MessaggioCompleto;
import bean.Paziente;
import bean.Utente;
import model.AnnuncioModel;
import model.MedicoModel;
import model.MessaggioModel;
import model.PazienteModel;
import utility.AlgoritmoCriptazioneUtility;

/**
 * Questa classe è una servlet che modella le operazioni comuni delle
 * funzionalità di comunicazione
 * 
 * @author nico
 */
@WebServlet("/comunicazione")
public class GestioneComunicazione extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GestioneComunicazione() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				request.setAttribute("notification", "Errore generato dalla richiesta!");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("./dashboard.jsp"); // TODO
																											// reindirizzamento
				// home
				dispatcher.forward(request, response);
				return;
			}

		} catch (Exception e) {
			System.out.println("Errore durante il caricamento della pagina:");
			e.printStackTrace();
		}

		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Metodo che carica i destinatari ammessi per inviare messaggi e annunci
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void caricaDestinatari(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		
		if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
			ArrayList<Medico> mediciCuranti = new ArrayList<>();
			Medico selezionato;
			for (String cf : ((Paziente) utente).getMedici()) {
				selezionato = MedicoModel.getMedicoByCF(cf);

				if (selezionato != null)
					mediciCuranti.add(selezionato);
			}
			request.setAttribute("mediciCuranti", mediciCuranti);
		}

		else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
			ArrayList<Paziente> pazientiSeguiti = new ArrayList<Paziente>();
			pazientiSeguiti.addAll(PazienteModel.getPazientiSeguiti(utente.getCodiceFiscale()));
			request.setAttribute("pazientiSeguiti", pazientiSeguiti);
		}

		else {
			System.out.println("L'utente deve essere loggato");
		}
	}

	/**
	 * Metodo che prende mittente, destinatari, oggetto, testo e allegato della
	 * comunicazione e lo salva nel database
	 * 
	 * @param request richiesta utilizzata per ottenere parametri e settare
	 *                attributi
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void inviaComunicazione(HttpServletRequest request, String operazione)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		Messaggio messaggio = null;
		Annuncio annuncio = null;
		Utente utente = (Utente) session.getAttribute("utente");
		ArrayList<String> destinatari = null;

		if ((boolean) session.getAttribute("accessDone") == true) {
			if (session.getAttribute("isPaziente") != null && (boolean) session.getAttribute("isPaziente") == true) {
				destinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectMedico")));
			} else if (session.getAttribute("isMedico") != null && (boolean) session.getAttribute("isMedico") == true) {
				destinatari = new ArrayList<String>(Arrays.asList(request.getParameterValues("selectPaziente")));
			}
			String CFMittente = utente.getCodiceFiscale();
			String oggetto = request.getParameter("oggetto");
			String testo = request.getParameter("testo");
			String allegato = null;
			String nomeFile = null;
			HashMap<String, Boolean> destinatariView = new HashMap<String,Boolean>();
			for(String temp: destinatari) {
				destinatariView.put(temp, false);
			}			
			
			InputStream fileStream = null;
			InputStream nomeFileStream = null;
			Part filePart = request.getPart("file");

			//check campi obbligatori
			if (controllaParametri(CFMittente, oggetto, testo)) {
				//check presenza e correttezza allegato
				if (filePart.getSize() > 0 && controllaFile(nomeFile, filePart.getSize())) {
					fileStream = filePart.getInputStream();
					nomeFile = filePart.getHeader("Content-Disposition").replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
					try {
						allegato = AlgoritmoCriptazioneUtility.codificaFile(fileStream);
						nomeFileStream = new ByteArrayInputStream(nomeFile.getBytes("UTF-8"));
						nomeFile = AlgoritmoCriptazioneUtility.codificaFile(nomeFileStream);
					} catch (Exception e) {
						System.out.println("inviaMessaggio: errore nella criptazione del file");
					} finally {
						if (fileStream != null && nomeFileStream != null) {
							fileStream.close();
							nomeFileStream.close();
						}
					}
				}

				if (operazione.equals("inviaMessaggio")) {
					messaggio = new MessaggioCompleto(CFMittente, destinatari, oggetto, testo, allegato, nomeFile,
							ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
					MessaggioModel.addMessaggio(messaggio);
				} else if (operazione.equals("inviaAnnuncio")) {
					annuncio = new AnnuncioCompleto(CFMittente, oggetto, testo, allegato, nomeFile,
							ZonedDateTime.now(ZoneId.of("Europe/Rome")), destinatariView);
					AnnuncioModel.addAnnuncio(annuncio);
				}

			} else {
				System.out.println("L'utente deve essere loggato");
			}
		}
	}

	public boolean controllaParametri(String codiceFiscale, String oggetto, String testo) {
		String expCodiceFiscale = "^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$";
		
		if (!Pattern.matches(expCodiceFiscale, codiceFiscale) || codiceFiscale.length() != 16) {
			return false;
		} else if (oggetto.length() < 1 || oggetto.length() > 75) {
			return false;
		} else if (testo.length() < 1 || testo.length() > 1000) {
			return false;
		}

		return true;
	}
	
	private boolean controllaFile(String nomeFile, long dimensioneFile) {
		String estensione = "";
		
		if (dimensioneFile == 0) {
			return false;
		}

		// file senza estensione (esistono, basta usare un sistema operativo vero)
		if (dimensioneFile > 0 && !nomeFile.contains(".")) {
			return false;
		}
		// senza questo controllo substring crasha in caso di nessun file e file senza
		// estensione
		if (dimensioneFile > 0 && nomeFile.contains(".")) {
			int indice = nomeFile.indexOf(".");
			estensione = nomeFile.substring(indice);
		}
		
		if (!estensione.equals("") && !estensione.equals(".jpg") && !estensione.equals(".jpeg")
				&& !estensione.equals(".png") && !estensione.equals(".pjpeg") && !estensione.equals(".pjp")
				&& !estensione.equals(".jfif") && !estensione.equals(".bmp")) {
			return false;
		} else if (dimensioneFile > 15728640l) {
			return false;
		}
		
		return true;
	}

}
