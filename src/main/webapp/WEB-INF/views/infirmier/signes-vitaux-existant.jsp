<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Accueil Patient Existant - Infirmier" />
</jsp:include>

<div class="max-w-4xl mx-auto space-y-6">
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center justify-between">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">US1 &bull; Étape 2a : Patient existant trouvé</h1>
            <p class="mt-1 text-sm text-slate-500">
                Informations du dossier et prise des nouveaux signes vitaux du jour
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/infirmier/recherche" class="text-sm font-medium text-slate-500 hover:text-slate-800">
            <i class="fa-solid fa-arrow-left mr-1"></i> Autre recherche
        </a>
    </div>

    <!-- Dossier existant du patient -->
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
        <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center justify-between">
            <span class="flex items-center">
                <i class="fa-solid fa-user-check text-emerald-600 mr-2"></i> Dossier de ${patient.nomComplet}
            </span>
            <span class="text-xs font-mono bg-slate-100 px-3 py-1 rounded-full text-slate-800 border border-slate-200">
                N° Sécu : ${patient.numSecu}
            </span>
        </h2>
        <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 text-sm">
            <div>
                <span class="text-xs text-slate-400 block uppercase font-semibold">Date de naissance</span>
                <span class="font-medium text-slate-800">${patient.dateNaissance}</span>
            </div>
            <div>
                <span class="text-xs text-slate-400 block uppercase font-semibold">Téléphone</span>
                <span class="font-medium text-slate-800">${patient.telephone}</span>
            </div>
            <div>
                <span class="text-xs text-slate-400 block uppercase font-semibold">Mutuelle</span>
                <span class="font-medium text-slate-800">${patient.mutuelle}</span>
            </div>
            <div>
                <span class="text-xs text-slate-400 block uppercase font-semibold">Adresse</span>
                <span class="font-medium text-slate-800">${patient.adresse}</span>
            </div>
        </div>
        <div class="mt-4 pt-3 border-t border-slate-100 grid grid-cols-1 sm:grid-cols-3 gap-4 text-xs">
            <div class="p-3 bg-slate-50 rounded-xl">
                <span class="font-semibold text-slate-700 block mb-1">Antécédents :</span>
                <p class="text-slate-600">${not empty patient.antecedents ? patient.antecedents : 'Aucun'}</p>
            </div>
            <div class="p-3 bg-slate-50 rounded-xl">
                <span class="font-semibold text-slate-700 block mb-1">Allergies :</span>
                <p class="text-slate-600">${not empty patient.allergies ? patient.allergies : 'Aucune'}</p>
            </div>
            <div class="p-3 bg-slate-50 rounded-xl">
                <span class="font-semibold text-slate-700 block mb-1">Traitements :</span>
                <p class="text-slate-600">${not empty patient.traitements ? patient.traitements : 'Aucun'}</p>
            </div>
        </div>
    </div>

    <!-- Formulaire de saisie des NOUVEAUX signes vitaux -->
    <form action="${pageContext.request.contextPath}/infirmier/existant" method="POST" class="space-y-6">
        <input type="hidden" name="csrfToken" value="${csrfToken}" />
        <input type="hidden" name="patientId" value="${patient.id}" />

        <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
            <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                <i class="fa-solid fa-heart-pulse text-sky-600 mr-2"></i> Saisie des Nouveaux Signes Vitaux
            </h2>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Tension artérielle *</label>
                    <input type="text" name="tension" required placeholder="ex: 120/80" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">mmHg</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Fréquence cardiaque *</label>
                    <input type="number" name="frequenceCardiaque" required min="30" max="250" placeholder="ex: 75" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">bpm (battements/min)</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Température corporelle *</label>
                    <input type="number" step="0.1" name="temperature" required min="30" max="45" placeholder="ex: 37.0" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">°C</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Fréquence respiratoire *</label>
                    <input type="number" name="frequenceRespiratoire" required min="5" max="60" placeholder="ex: 16" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">cycles/min</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Poids (kg)</label>
                    <input type="number" step="0.1" name="poids" min="1" max="300" placeholder="ex: 70.0" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Taille (cm)</label>
                    <input type="number" step="0.5" name="taille" min="30" max="250" placeholder="ex: 172" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                </div>
            </div>
        </div>

        <div class="flex justify-end space-x-3 pt-2">
            <a href="${pageContext.request.contextPath}/infirmier/recherche"
               class="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-semibold transition">
                Annuler
            </a>
            <button type="submit"
                    class="px-6 py-2.5 bg-sky-600 hover:bg-sky-700 text-white rounded-xl text-sm font-semibold shadow-md transition flex items-center space-x-2">
                <i class="fa-solid fa-plus"></i>
                <span>Enregistrer & Ajouter à la file d'attente</span>
            </button>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
