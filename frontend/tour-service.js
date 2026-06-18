const API_URL = '/api/v1/excursion';
const form = document.getElementById('excursionForm');
const tableBody = document.getElementById('excursionsTableBody');
const submitBtn = document.getElementById('submitBtn');
const cancelBtn = document.getElementById('cancelBtn');
const formTitle = document.getElementById('formTitle');


async function loadExcursions() {
    try {
        const response = await fetch(API_URL);
        const pageData = await response.json();
        const excursions = pageData.content;
        tableBody.innerHTML = '';
        excursions.forEach(item => {
            const safeName = item.name.replace(/'/g, "\\'");
            const safeGuide = item.guideName.replace(/'/g, "\\'");
            tableBody.innerHTML += `
                        <tr class="border-b hover:bg-gray-50">
                            <td class="p-3 border-r border-gray-100">${item.id}</td>
                            <td class="p-3 border-r border-gray-100 font-medium text-gray-900">${item.name}</td>
                            <td class="p-3 border-r border-gray-100">${item.price.toFixed(2)} руб.</td>
                            <td class="p-3 border-r border-gray-100 text-xs text-gray-500">${item.from} — ${item.to}</td>
                            <td class="p-3 border-r border-gray-100">${item.guideName}</td>
                            <td class="p-3 border-r border-gray-100">
                                <span class="px-2 py-1 rounded-full text-xs font-medium ${item.excursionType === 'Групповая' ? 'bg-blue-100 text-blue-800' : 'bg-purple-100 text-purple-800'}">
                                    ${item.excursionType}
                                </span>
                            </td>
                            <td class="p-3 border-r border-gray-100 text-center text-lg">
                                ${item.lunchIncluded ? '✅' : '❌'}
                            </td>
                            <td class="p-3 text-center whitespace-nowrap">
                                <button onclick="editExcursion(${item.id})" title="Редактировать"
                                        class="inline-flex items-center justify-center bg-amber-50 hover:bg-amber-100 text-amber-700 border border-amber-200 rounded p-1.5 mr-2 transition cursor-pointer">
                                    ✏️
                                </button>

                                <button onclick="deleteExcursion(${item.id})" title="Удалить"
                                        class="inline-flex items-center justify-center bg-red-50 hover:bg-red-100 text-red-700 border border-red-200 rounded p-1.5 transition cursor-pointer">
                                    🗑️
                                </button>
                            </td>
                        </tr>
                    `;
        });
    } catch (error) {
        console.error('Ошибка при загрузке:', error);
    }
}

form.addEventListener('submit', async(e) => {
    e.preventDefault();
    const id = document.getElementById('excursionId').value;
    const payload = {
        name: document.getElementById('name').value,
        price: parseFloat(document.getElementById('price').value),
        from: document.getElementById('from').value,
        to: document.getElementById('to').value,
        guideName: document.getElementById('guideName').value,
        excursionType: document.getElementById('excursionType').value,
        lunchIncluded: document.getElementById('lunchIncluded').checked
    };
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;
    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });
        if (response.ok) {
            resetForm();
            loadExcursions();
        } else {
            alert('Ошибка при сохранении! Статус: ' + response.status);
        }
    } catch (error) {
        console.error('Ошибка при отправке:', error);
    }
});

async function deleteExcursion(id) {
    if (!confirm('Удалить эту экскурсию?')) return;
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            loadExcursions();
        } else {
            alert('Ошибка при удалении! Статус: ' + response.status);
        }
    } catch (error) {
        console.error('Ошибка при удалении:', error);
    }
}

async function editExcursion(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) {
            alert(`Не удалось загрузить данные экскурсии. Статус: ${response.status}`);
            return;
        }
        const excursion = await response.json();
        document.getElementById('excursionId').value = excursion.id;
        document.getElementById('name').value = excursion.name;
        document.getElementById('price').value = excursion.price;
        document.getElementById('from').value = excursion.from;
        document.getElementById('to').value = excursion.to;
        document.getElementById('guideName').value = excursion.guideName;
        document.getElementById('excursionType').value = excursion.excursionType;
        document.getElementById('lunchIncluded').checked = excursion.lunchIncluded;

        formTitle.innerText = 'Редактировать экскурсию #' + id;
        submitBtn.innerText = 'Обновить';
        cancelBtn.classList.remove('hidden');
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    } catch (error) {
        console.error('Ошибка при получении экскурсии по ID:', error);
        alert('Произошла ошибка при загрузке данных с сервера.');
    }
}

function resetForm() {
    form.reset();
    document.getElementById('excursionId').value = '';
    formTitle.innerText = 'Добавить новую экскурсию';
    submitBtn.innerText = 'Сохранить';
    cancelBtn.classList.add('hidden');
}

cancelBtn.addEventListener('click', resetForm);
loadExcursions();