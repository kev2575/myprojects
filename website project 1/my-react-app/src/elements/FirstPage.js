import './FirstPage.css';
import Card from './Card';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
function FirstPage() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    axios
      .get('http://localhost:8082/api/items')
      .then((res) => {
        setItems(res.data);
      })
      .catch((err) => {
        console.log('Error from FirstPage');
      });
  }, []);

  const itemList =
    items.length === 0
      ? 'there is no item record!'
      : items.map((item, k) => <Card item={item}  key={k} edit={false} />);

   <p>
    <a href="/create-item">+ Add New Item</a>
   </p>   
      return (
        
        <div className="App">
          <div className="Menu">
            <Link
              to='/Login'
              className='menu-button'
            >
              Log in
            </Link>
          </div>
          <div className="List">
            {itemList}
          </div>
        </div>
      );
}
export default FirstPage;
