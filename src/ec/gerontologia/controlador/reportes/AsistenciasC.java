package ec.gerontologia.controlador.reportes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.AsistenciaDAO;
import ec.gerontologia.modelo.Clase;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Matricula;
import ec.gerontologia.modelo.MatriculaDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.util.PrintReport;

@SuppressWarnings("serial")
public class AsistenciasC extends SelectorComposer<Component>{
	@Wire private Window winRegistroAsistencias;
	@Wire private Textbox txtIdClase;
	@Wire private Button btnBuscarClase;
	@Wire private Datebox dtpFechaClase;
	@Wire private Textbox txtDescripcion;
	@Wire private Textbox txtObservacion;
	@Wire private Listbox lstPersonas;
	@Wire private Button btnRegistrar;
	
	
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	Clase claseAsistencia;
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	
	List<Matricula> matriculas;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			Clients.showNotification("No tiene un centro asignado!");
			txtIdClase.setDisabled(true);
			btnBuscarClase.setDisabled(true);
			dtpFechaClase.setDisabled(true);
			txtDescripcion.setDisabled(true);
			txtObservacion.setDisabled(true);
			btnRegistrar.setDisabled(true);
			return;
		}
		
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() == 0) {
			Clients.showNotification("No hay periodos en proceso!");
			txtIdClase.setDisabled(true);
			btnBuscarClase.setDisabled(true);
			dtpFechaClase.setDisabled(true);
			txtDescripcion.setDisabled(true);
			txtObservacion.setDisabled(true);
			btnRegistrar.setDisabled(true);
			return;
		}
	}
	
	@Listen("onClick=#btnBuscarClase")
	public void buscarClase() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/reportes/seleccionarClase.zul", winRegistroAsistencias, params);
		ventanaCargar.doModal();
	}
	
	public void recuperarClase(Clase claseRecuperado) {
		claseAsistencia = claseRecuperado;
		txtIdClase.setText(String.valueOf(claseRecuperado.getIdClase()));
		txtDescripcion.setText(claseRecuperado.getDescripcion());
		txtObservacion.setText(claseRecuperado.getObservacion());
		dtpFechaClase.setValue(claseRecuperado.getFecha());
		recuperarPersonas();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void recuperarPersonas() {
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			return;
		}
		Integer idPeriodo = 0;
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() == 0) {
			return;
		}
		idPeriodo = periodos.get(0).getIdPeriodo();
		
		List<Matricula> listaMatricula = matriculaDAO.getPersonaMatriculados(idPeriodo);
		lstPersonas.setModel(new ListModelList(listaMatricula));
	}
	
	public List<Matricula> getMatriculas() {
		return matriculas;
	}
	
	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
	
	AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
	@Listen("onClick=#btnImprimir")
	public void imprimir() {
		try {
			List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
			if(encargado.size() == 0) {
				return;
			}
			
			PrintReport reporte = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ID_CLASE", claseAsistencia.getIdClase());
			param.put("TITULO", "CENTRO GERONTOLÓGICO " + encargado.get(0).getCentro().getNombreCentro());
			reporte.crearReporte("/recursos/reportes/asistencia.jasper", matriculaDAO, param);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void limpiar() {
		claseAsistencia = null;
		txtIdClase.setText("");
		txtDescripcion.setText("");
		txtObservacion.setText("");
		dtpFechaClase.setValue(new Date());
		List<Matricula> listaMatricula = new ArrayList<Matricula>();
		lstPersonas.setModel(new ListModelList(listaMatricula));
	}
}