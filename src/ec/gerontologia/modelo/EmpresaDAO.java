package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class EmpresaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Empresa> getEmpresaActivo() {
		List<Empresa> resultado; 
		Query query = getEntityManager().createNamedQuery("Empresa.buscarActivo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Empresa>) query.getResultList();
		return resultado;
	}
}
