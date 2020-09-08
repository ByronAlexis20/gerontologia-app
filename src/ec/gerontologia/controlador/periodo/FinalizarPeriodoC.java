package ec.gerontologia.controlador.periodo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;

@SuppressWarnings("serial")
public class FinalizarPeriodoC extends SelectorComposer<Component>{
	@Wire private Window winPeriodoFinalizar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	@Wire private Datebox dtpFechaInicio;
	@Wire private Datebox dtpFechaFin;
	@Wire private Combobox cboEstadoPeriodo;

	private PeriodoListaC periodoListaC;
	private PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	private PeriodoLectivo periodo;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		periodoListaC = (PeriodoListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		periodo = null; 
		if (Executions.getCurrent().getArg().get("Periodo") != null) {
			periodo = (PeriodoLectivo) Executions.getCurrent().getArg().get("Periodo");
		}else{
			periodo = new PeriodoLectivo(); 
		}
	}


	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(cboEstadoPeriodo.getSelectedItem() == null) {
				Clients.showNotification("Por favor ingrese descripción.!!");
				txtDescripcion.focus();
				return resultado;
			}			
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Listen("onClick=#btnGrabar")
	public void grabar(){
		System.out.println("entra grabando");
		try {
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				periodoDAO.getEntityManager().getTransaction().begin();
				periodo.setEstado("A");
				if (periodo.getIdPeriodo() != null) {
					periodo.setEstadoPeriodo(cboEstadoPeriodo.getValue());
					periodo = (PeriodoLectivo) periodoDAO.getEntityManager().merge(periodo);
				}
				// Cierra la transaccion.
				periodoDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				periodoListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			periodoDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winPeriodoFinalizar.detach();
	}


	public PeriodoLectivo getPeriodo() {
		return periodo;
	}


	public void setPeriodo(PeriodoLectivo periodo) {
		this.periodo = periodo;
	}
}
