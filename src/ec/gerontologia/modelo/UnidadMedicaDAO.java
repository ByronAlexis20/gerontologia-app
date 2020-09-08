package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class UnidadMedicaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<UnidadMedica> getUnidadesMedicas(String value) {
		List<UnidadMedica> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("UnidadMedica.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<UnidadMedica>) query.getResultList();
		return resultado;
	}
}
