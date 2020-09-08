package ec.gerontologia.controlador.reportes;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Clase;
import ec.gerontologia.modelo.ClasesDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;

@SuppressWarnings("serial")
public class SeleccionarClaseC extends SelectorComposer<Component>{
	@Wire private Window winSeleccionarClaseLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstClases;
	
	private List<Clase> clases;
	private Clase claseSeleccionado;
	
	ClasesDAO claseDAO = new ClasesDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	
	AsistenciasC asistenciaC;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		buscar();
		asistenciaC = (AsistenciasC)Executions.getCurrent().getArg().get("VentanaPadre");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		if (clases != null) {
			clases = null; 
		}
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			Clients.showNotification("No tiene un centro asignado!");
			return;
		}
		
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() == 0) {
			Clients.showNotification("No hay periodos en proceso!");
			return;
		}
		
		
		
		clases = claseDAO.getClasePorPeriodo(periodos.get(0).getIdPeriodo(), txtBuscar.getValue());
		lstClases.setModel(new ListModelList(clases));
		claseSeleccionado = null;
	}
	@Listen("onClick=#btnAceptar")
	public void aceptar(){
		if(claseSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		asistenciaC.recuperarClase(claseSeleccionado);
		salir();
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winSeleccionarClaseLista.detach();
	}

	public List<Clase> getClases() {
		return clases;
	}

	public void setClases(List<Clase> clases) {
		this.clases = clases;
	}

	public Clase getClaseSeleccionado() {
		return claseSeleccionado;
	}

	public void setClaseSeleccionado(Clase claseSeleccionado) {
		this.claseSeleccionado = claseSeleccionado;
	}
	

}
