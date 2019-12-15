package control;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import bean.Paziente;
import bean.SchedaParametri;
import model.SchedaParametriModel;

@WebServlet("/GestioneParametri")
public class GestioneParametri extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			//Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona ok)
			if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(new Gson().toJson("Errore generato dalla richiesta!"));
				return;
			}
			
			String flag = request.getParameter("flag");
			
			//Download report
			if(flag.equals("0")) {
					
			}
			
			//Visualizza la scheda dei parametri del paziente selezionati
			if(flag.equals("2")) {
				monitoraParametri(request, response);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(""); //reindirizzamento view per la visualizzazione delle schede
				dispatcher.forward(request, response);
			}
			
		}catch (Exception e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// Verifica del tipo di chiamata alla servlet (sincrona o asinconrona)(sincrona
		// ok)
		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(new Gson().toJson("Errore generato dalla richiesta!"));
				return;
			}

			// Inserisci parametri nella scheda
			
			// Valutare la possibilità di inserire flag di controllo anche se questo è
			// l'unico metodo eseguito da una post request
			inserisciParametri(request.getParameter("PazienteCodiceFiscale"), request.getParameter("Peso"),
					request.getParameter("PaMin"), request.getParameter("PaMax"),
					request.getParameter("ScaricoIniziale"), request.getParameter("UF"),
					request.getParameter("TempoSosta"), request.getParameter("Scarico"), 
					request.getParameter("Carico"));

			response.sendRedirect(request.getContextPath() + "/view/index.jsp");
		} catch (IOException e) {
			System.out.println("Errore in gestione parametri:");
			e.printStackTrace();
		}
		return;
	}
	
	/**Questo metodo richiama dal database la lista delle schede parametri inserite da un determinato utente.
	 * Nel caso in cui la visualizzazione sia richiesta da un paziente, esso è richiamato dalla sessione,
	 * nel caso in cui la visualizzazione sia richiesta da un medico, il paziente di cui mostrare le schede
	 * è inserito in un attributo nella request
	 * @param request
	 * @param response
	 */
	private void monitoraParametri(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String pazienteCF;
		Paziente pazienteLoggato = (Paziente)session.getAttribute("pazienteLoggato");
		
		if (session.getAttribute("pazienteLoggatoCF") != null)
			pazienteCF = pazienteLoggato.getCodiceFiscale();
		else	
			pazienteCF = request.getParameter("codiceFiscale");
		
		ArrayList<SchedaParametri> sp = SchedaParametriModel.getSchedaParametriByCF(pazienteCF);
		request.setAttribute("schedaParametri", sp);
	}
	
	/**Questo metodo inserisce nel database una SchedaParametri formata dai dati inseriti dall'utente.
	 * Riceve le stringhe inserite dall'utente, prelevate dalla request. 
	 * Dopo averle parsate opportunamente, le usa per istanziare un bean SchedaParametri
	 * e usa un'istanza di SchedaParametriModel per caricarla sul database.
	 * 
	 * @param cf
	 * @param peso
	 * @param paMin
	 * @param paMax
	 * @param scaricoIniziale
	 * @param uf
	 * @param tempoSosta
	 * @param scarico
	 * @param carico
	 * @param data
	 * 
	 * @author nico
	 */
	private void inserisciParametri(String cf, String peso, String paMin, String paMax, String scaricoIniziale, String uf, String tempoSosta, String scarico, String carico)
	{
		SchedaParametri daAggiungere;
		
		//valutare la possibilità di fare controlli sulle stringhe ottenute prima di parsarle
		
		BigDecimal newPeso = new BigDecimal(peso);
		int newPaMin = Integer.parseInt(paMin);
		int newPaMax = Integer.parseInt(paMax);
		int newScaricoIniziale = Integer.parseInt(scaricoIniziale);
		int newUf = Integer.parseInt(uf);
		int newTempoSosta = Integer.parseInt(tempoSosta);
		int newScarico = Integer.parseInt(scarico);
		int newCarico = Integer.parseInt(carico);
		LocalDate oggi = LocalDate.now();
				
		daAggiungere = new SchedaParametri(cf, newPeso, newPaMin, newPaMax, newScaricoIniziale, 
				newUf, newTempoSosta, newScarico, newCarico, oggi);
		
		SchedaParametriModel.addSchedaParametri(daAggiungere);
	}
	
}