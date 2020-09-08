package ec.gerontologia.controlador.reportes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.CentroDAO;
import ec.gerontologia.modelo.Matricula;
import ec.gerontologia.modelo.MatriculaDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.util.PrintReport;

@SuppressWarnings("serial")
public class PersonaCentroC extends SelectorComposer<Component>{
	@Wire private Window winPersonaCentro;
	@Wire private Button btnImprimir;
	@Wire private Listbox lstPersonas;
	@Wire private Combobox cboCentros;
	
	private List<Matricula> personas;
	CentroDAO centroDAO = new CentroDAO();
	MatriculaDAO personaDAO = new MatriculaDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		if(cboCentros.getSelectedItem() == null) {
			Clients.showNotification("Debe seleccionar un centro");
			return;
		}
		Centro centroSeleccionado = (Centro)cboCentros.getSelectedItem().getValue();
		Integer idPeriodo = 0;
		List<PeriodoLectivo> listaPeriodo = periodoDAO.getPeriodoCentroDesc(centroSeleccionado.getIdCentro());
		if(listaPeriodo.size() > 0)
			idPeriodo = listaPeriodo.get(0).getIdPeriodo();
		List<Matricula> matriculas = personaDAO.getPersonaMatriculados(idPeriodo);
		
		System.out.println(matriculas.size() + " - " + idPeriodo);
		lstPersonas.setModel(new ListModelList(matriculas));
	}
	
	
	@Listen("onClick=#btnImprimir")
	public void imprimir() {
		Centro centroSeleccionado = (Centro)cboCentros.getSelectedItem().getValue();
		Integer idPeriodo = 0;
		List<PeriodoLectivo> listaPeriodo = periodoDAO.getPeriodoCentroDesc(centroSeleccionado.getIdCentro());
		if(listaPeriodo.size() > 0)
			idPeriodo = listaPeriodo.get(0).getIdPeriodo();
		
		PrintReport reporte = new PrintReport();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID_PERIODO", idPeriodo);
		param.put("TITULO", "LISTADO DE PERSONAS MATRICULADOS EN EL CENTRO " + centroSeleccionado.getNombreCentro());
		reporte.crearReporte("/recursos/reportes/listaPersonas.jasper", personaDAO, param);
	}
	
	public List<Centro> getCentros(){
		List<Centro> lista = centroDAO.getCentros("");
		return lista;
	}
	
	public List<Matricula> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Matricula> personas) {
		this.personas = personas;
	}
	
	
	
}
