import './FirstPage.css';
import Card from './Card';
import { useState, useEffect} from 'react';
import { Link} from 'react-router-dom';
import axios from 'axios';
function SecondPage(props) {
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
      : items.map((item, k) => <Card item={item}  key={k} edit={true} />);

      return (
        <div className="App">
           <div className="Menu">
            <Link
              to='/CreateItem'
              className='menu-button'
            >
              Create Item
            </Link>
            <Link
                to='/'
                className='menu-button'
            >
              Logout
            </Link>
            </div>
          <div className="List">
            {itemList}
          </div>
        </div>
      );
        
}
export default SecondPage;