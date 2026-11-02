import { useEffect, useState } from "react";
import "./App.css";

const emptyForm = { name: "", price: "", quantity: "" };

export default function App() {
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");

  async function loadProducts() {
    const response = await fetch("/api/products");
    if (!response.ok) throw new Error("Could not load products");
    setProducts(await response.json());
  }

  useEffect(() => {
    loadProducts().catch(e => setError(e.message));
  }, []);

  function change(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function submit(e) {
    e.preventDefault();
    setError("");

    try {
      const response = await fetch(
          editingId ? `/api/products/${editingId}` : "/api/products",
          {
            method: editingId ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              name: form.name,
              price: Number(form.price),
              quantity: Number(form.quantity)
            })
          }
      );

      if (!response.ok) {
        throw new Error("Save failed. Check your input.");
      }

      setForm(emptyForm);
      setEditingId(null);
      await loadProducts();
    } catch (e) {
      setError(e.message);
    }
  }

  function edit(product) {
    setEditingId(product.id);
    setForm({
      name: product.name,
      price: String(product.price),
      quantity: String(product.quantity)
    });
  }

  async function remove(id) {
    if (!window.confirm("Delete this product?")) return;

    try {
      const response = await fetch(`/api/products/${id}`, {
        method: "DELETE"
      });
      if (!response.ok) throw new Error("Delete failed");
      await loadProducts();
    } catch (e) {
      setError(e.message);
    }
  }

  return (
      <main>
        <h1>Product Management</h1>
        {error && <p role="alert">{error}</p>}

        <form onSubmit={submit}>
          <input name="name" placeholder="Product name"
                 value={form.name} onChange={change} required />
          <input name="price" type="number" min="0" step="0.01"
                 placeholder="Price" value={form.price}
                 onChange={change} required />
          <input name="quantity" type="number" min="0" step="1"
                 placeholder="Quantity" value={form.quantity}
                 onChange={change} required />

          <button type="submit">
            {editingId ? "Update Product" : "Add Product"}
          </button>
          {editingId && (
              <button type="button" onClick={() => {
                setEditingId(null);
                setForm(emptyForm);
              }}>Cancel</button>
          )}
        </form>

        <table>
          <thead>
          <tr>
            <th>ID</th><th>Name</th><th>Price</th>
            <th>Quantity</th><th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {products.map(product => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>{product.price}</td>
                <td>{product.quantity}</td>
                <td>
                  <button onClick={() => edit(product)}>Edit</button>
                  <button onClick={() => remove(product.id)}>Delete</button>
                </td>
              </tr>
          ))}
          </tbody>
        </table>
      </main>
  );
}