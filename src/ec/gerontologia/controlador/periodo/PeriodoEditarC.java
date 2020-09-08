package ec.gerontologia.controlador.periodo;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;

@SuppressWarnings("serial")
public class PeriodoEditarC extends SelectorComposer<Component>{
	@Wire private Window winPeriodoEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	@Wire private Datebox dtpFechaInicio;
	@Wire private Datebox dtpFechaFin;

	private PeriodoListaC periodoListaC;
	private PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	private PeriodoLectivo periodo;
	Integer idCentro = 0;

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
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		idCentro = encargado.get(0).getCentro().getIdCentro();
	}


	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(txtDescripcion.getText() == null) {
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
				if (periodo.getIdPeriodo() == null) {
					System.out.println(idCentro);
					periodo.setIdcentro(idCentro);

					periodo.setEstadoPeriodo("EN PROCESO");
					periodoDAO.getEntityManager().persist(periodo);
				}else{
					System.out.println(idCentro);
					periodo.setIdcentro(idCentro);

					periodo.setEstadoPeriodo("EN PROCESO");
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
		winPeriodoEditar.detach();
	}


	public PeriodoLectivo getPeriodo() {
		return periodo;
	}


	public void setPeriodo(PeriodoLectivo periodo) {
		this.periodo = periodo;
	}
}
