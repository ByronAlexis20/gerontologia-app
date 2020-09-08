package ec.gerontologia.controlador.administracion;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Instruccion;
import ec.gerontologia.modelo.InstruccionDAO;

@SuppressWarnings({ "serial", "rawtypes" })
public class InstruccionEditarC extends SelectorComposer{
	@Wire private Window winInstruccionEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	
	private InstruccionListaC instruccionListaC;
	private InstruccionDAO instruccionDAO = new InstruccionDAO();
	private Instruccion instruccion;
	
	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		instruccionListaC = (InstruccionListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		instruccion = null; 
		if (Executions.getCurrent().getArg().get("Instruccion") != null) {
			instruccion = (Instruccion) Executions.getCurrent().getArg().get("Instruccion");
		}else{
			instruccion = new Instruccion(); 
		}
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
				instruccionDAO.getEntityManager().getTransaction().begin();
				instruccion.setEstado("A");
				if (instruccion.getIdInstruccion() == null) {
					instruccionDAO.getEntityManager().persist(instruccion);
				}else{
					instruccion = (Instruccion) instruccionDAO.getEntityManager().merge(instruccion);
				}
				// Cierra la transaccion.
				instruccionDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				instruccionListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			instruccionDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winInstruccionEditar.detach();
	}


	public Instruccion getInstruccion() {
		return instruccion;
	}


	public void setInstruccion(Instruccion instruccion) {
		this.instruccion = instruccion;
	}
	
	
}
