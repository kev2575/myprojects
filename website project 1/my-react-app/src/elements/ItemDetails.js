import './FirstPage.css';
import Card from './Card';
import { useState, useEffect} from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
function ItemDetails() {
    const [item, setItem] = useState({});

  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`http://localhost:8082/api/items/${id}`)
      .then((res) => {
        setItem(res.data);
      })
      .catch((err) => {
        console.log('Error from SecondPage');
      });
  }, [id]);

  const onDeleteClick = (id) => {
    axios
      .delete(`http://localhost:8082/api/items/${id}`)
      .then((res) => {
        navigate('/');
      })
      .catch((err) => {
        console.log('Error form SecondPage_deleteClick');
      });
  };
  const itemCard = (
    <Card item = {item}/>
  )
    return(
        <div className="App">
           <div className="Menu">
           <button
              type='button'
              className='delete'
              onClick={() => {
                onDeleteClick(item._id);
              }}
            >
              Delete Item
            </button>
           <Link
              to='/'
              className='login'
            >
             Log Out
            </Link>
            </div>
          <div className="List">
            {itemCard}
          </div>
          <Link
              to={`/EditPage/${item._id}`}
              className='edit button'
            >
              Edit Item
            </Link>
        </div>
    )
}
export default ItemDetails;