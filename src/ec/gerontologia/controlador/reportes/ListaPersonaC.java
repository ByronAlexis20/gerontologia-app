package ec.gerontologia.controlador.reportes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Matricula;
import ec.gerontologia.modelo.MatriculaDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.util.PrintReport;

@SuppressWarnings("serial")
public class ListaPersonaC extends SelectorComposer<Component>{
	@Wire private Window winPersonaLista;
	@Wire private Button btnImprimir;
	@Wire private Listbox lstPersona;
	
	private List<Matricula> personas;
	MatriculaDAO personaDAO = new MatriculaDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	
	Integer idPeriodo = 0;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			return;
		}
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() == 0) {
			return;
		}
		
		idPeriodo = periodos.get(0).getIdPeriodo();
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		List<Matricula> matriculas = personaDAO.getPersonaMatriculados(idPeriodo);
		System.out.println(matriculas.size() + " - " + idPeriodo);
		lstPersona.setModel(new ListModelList(matriculas));
	}
	
	@Listen("onClick=#btnImprimir")
	public void imprimir() {
		PrintReport reporte = new PrintReport();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID_PERIODO", idPeriodo);
		param.put("TITULO", "LISTADO DE PERSONAS MATRICULADOS EN EL CENTRO");
		reporte.crearReporte("/recursos/reportes/listaPersonas.jasper", personaDAO, param);
	}

	public List<Matricula> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Matricula> personas) {
		this.personas = personas;
	}
	
	

}
