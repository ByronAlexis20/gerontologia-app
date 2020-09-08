package ec.gerontologia.controlador.administracion;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

import ec.gerontologia.modelo.Empresa;
import ec.gerontologia.modelo.EmpresaDAO;

@SuppressWarnings("serial")
public class EmpresaC extends SelectorComposer<Component>{
	@Wire private Textbox txtRuc;
	@Wire private Textbox txtRazonSocial;
	@Wire private Textbox txtRepresentante;
	@Wire private Textbox txtEmail;
	@Wire private Textbox txtDireccion;
	EmpresaDAO empresaDAO = new EmpresaDAO();
	Empresa empresa = new Empresa();
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		List<Empresa> lista = empresaDAO.getEmpresaActivo();
		if(lista.size() > 0) {
			txtRuc.setText(lista.get(0).getRuc());
			txtRazonSocial.setText(lista.get(0).getRazonSocial());
			txtRepresentante.setText(lista.get(0).getRepresentante());
			txtEmail.setText(lista.get(0).getEmail());
			txtDireccion.setText(lista.get(0).getDireccion());
			empresa = lista.get(0);
		}
	}
	@Listen("onClick=#btnGrabar")
	public void grabar(){
		System.out.println("entra grabando");
		try {
			if(empresa.getIdEmpresa() != null) {
				empresa.setDireccion(txtDireccion.getText());
				empresa.setEmail(txtEmail.getText());
				empresa.setEstado("A");
				empresa.setRazonSocial(txtRazonSocial.getText());
				empresa.setRepresentante(txtRepresentante.getText());
				empresa.setRuc(txtRuc.getText());
				empresaDAO.getEntityManager().getTransaction().begin();
				empresaDAO.getEntityManager().merge(empresa);
				empresaDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Datos grabados correctamente");
			}
		} catch (Exception e) {
			e.printStackTrace();
			empresaDAO.getEntityManager().getTransaction().rollback();
		}
	}
}
